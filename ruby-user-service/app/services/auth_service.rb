class AuthService
  require "jwt"

  JWT_SECRET_KEY = "your_hardcoded_jwt_secret_key"

  def self.login(email, password)
    worker = Worker.find_by(email: email)
    return generate_jwt(worker) if authenticate(worker, password) && worker.active

    user = User.find_by(email: email)
    return generate_jwt(user) if authenticate(user, password) && user.active

    nil
  end

  def self.authenticate_user(token)
    payload = decode_jwt(token)
    return nil unless payload

    if payload.key?('permissions')
      return Worker.find_by(id: payload['id'])
    else
      return User.find_by(id: payload['id'])
    end
  rescue ActiveRecord::RecordNotFound
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

  def self.decode_jwt(token)
    JWT.decode(token, JWT_SECRET_KEY, true, algorithm: 'HS256').first
  rescue JWT::DecodeError
    nil
  end

  def self.authenticate(entity, password)
    return false unless entity
    PasswordEncryptor.matches?(entity.password_digest, password)
  end
end