class OneTimePassword < ApplicationRecord
  validates :email, presence: { message: "email can't be blank" }, format: { with: URI::MailTo::EMAIL_REGEXP }

  validates :one_time_password, presence: { message: "one time password can't be blank" }, length: { equal: 6, message: "one time password has to be 6 characters long" }

  validates :expiration, presence: { message: "expiration can't be blank" }
  validate :validate_expiration

  private

  def validate_expiration
    return unless expiration.present?

    errors.add(:expiration, "expiration must be in the future") if expiration <= Time.now.to_i * 1000
  end
end
