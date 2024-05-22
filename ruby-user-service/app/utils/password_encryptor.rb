require "bcrypt"

class PasswordEncryptor
  def self.encrypt(password)
    BCrypt::Password.create(password)
  end

  def self.matches?(encrypted_password, password)
    BCrypt::Password.new(encrypted_password) == password
  end
end
