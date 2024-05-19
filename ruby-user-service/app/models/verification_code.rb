class VerificationCode < ApplicationRecord
  validates :email, presence: { message: "email can't be blank" }, format: { with: URI::MailTo::EMAIL_REGEXP }
  validates :code, presence: { message: "code can't be blank" }
  validates :expiration, presence: { message: "expiration can't be blank" }
  validate :validate_expiration
  validates :reset, inclusion: { in: [true, false], message: "reset can't be blank" }

  private

  def validate_expiration
    return unless expiration.present?

    errors.add(:expiration, "expiration must be in the future") if expiration <= Time.now.to_i * 1000
  end
end
