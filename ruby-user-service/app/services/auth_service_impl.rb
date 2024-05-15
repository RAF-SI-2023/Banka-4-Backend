class AuthServiceImpl
  require "jwt"
  include AuthService

  JWT_SECRET_KEY = "your_hardcoded_jwt_secret_key"

  def self.login(email, password)
    worker = Worker.find_by(email: email)
    return generate_jwt(worker) if authenticate(worker, password)

    user = User.find_by(email: email)
    return generate_jwt(user) if authenticate(user, password)

    nil
  end

  private

  def self.generate_jwt(entity)
    return unless entity

    payload = {
      id: entity.id,
      email: entity.email,
      permissions: entity.respond_to?(:permissions) ? entity.permissions : nil
    }

    JWT.encode(payload, JWT_SECRET_KEY, 'HS256')
  end

  def self.authenticate(entity, password)
    PasswordEncryptor.matches?(entity.password_digest, password)
  end
end
