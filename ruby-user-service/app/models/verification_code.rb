class VerificationCode < ApplicationRecord
  validates :email, presence: { message: "email can't be blank" }, format: { with: URI::MailTo::EMAIL_REGEXP }
  validates :code, presence: { message: "code password can't be blank" }
  validates :expiration, presence: { message: "expiration can't be blank" }
  validate :validate_expiration
  validates :reset, inclusion: { in: [true, false], message: "reset can't be blank" }

  after_save :send_verification_email

  private

  def validate_expiration
    return unless expiration.present?

    errors.add(:expiration, "expiration must be in the future") if expiration <= Time.now.to_i * 1000
  end

  def send_verification_email
    VerificationCodeMailer.with(verification_code: self).send_verification_code_email.deliver_later
  end
end
