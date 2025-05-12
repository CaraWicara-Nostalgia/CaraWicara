package com.karina.carawicara.data.repository

import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.entity.UserEntity
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository(
    private val database: CaraWicaraDatabase
) {
    private val userDao = database.userDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun registerUser(email: String, password: String): Result<Long> {
        return try {
            // Check if email already exists
            if (userDao.checkEmailExists(email) > 0) {
                return Result.failure(Exception("Email sudah terdaftar"))
            }

            // Generate salt for password
            val salt = generateSalt()

            // Hash password with salt
            val hashedPassword = hashPassword(password, salt)

            // Create user entity
            val user = UserEntity(
                email = email,
                password = hashedPassword,
                salt = salt,
                createdAt = dateFormat.format(Date())
            )

            // Insert user and return ID
            val userId = userDao.insertUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("Email tidak terdaftar"))

            val hashedPassword = hashPassword(password, user.salt ?: "")

            if (user.password != hashedPassword) {
                return Result.failure(Exception("Password salah"))
            }

            userDao.updateLastLogin(user.id, dateFormat.format(Date()))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String): Result<Boolean> {
        return try {
            val user = userDao.getUserById(userId)
                ?: return Result.failure(Exception("User tidak ditemukan"))

            // Verify old password
            val hashedOldPassword = hashPassword(oldPassword, user.salt ?: "")
            if (user.password != hashedOldPassword) {
                return Result.failure(Exception("Password lama salah"))
            }

            // Generate new salt and hash new password
            val newSalt = generateSalt()
            val hashedNewPassword = hashPassword(newPassword, newSalt)

            // Update user
            val updatedUser = user.copy(
                password = hashedNewPassword,
                salt = newSalt
            )
            userDao.updateUser(updatedUser)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logoutUser(userId: Long) {
        userDao.updateLastLogin(userId, dateFormat.format(Date()))
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        val combined = password + salt
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(combined.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}