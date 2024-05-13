class User < ApplicationRecord
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable, :trackable and :omniauthable
  devise :database_authenticatable, :registerable, :recoverable, :rememberable, :validatable

  validates :first_name, presence: { message: "first name can't be blank" }, format: { with: /\A[a-zA-Z]+\z/, message: "only allows letters" }
  validates :last_name, presence: { message: "last name can't be blank" }, format: { with: /\A[a-zA-Z]+\z/, message: "only allows letters" }

  validates :jmbg, presence: { message: "jmbg can't be blank" }, uniqueness: { message: "jmbg has to be unique" }, length: { is: 13 }, format: { with: /\A[0-9]+\z/, message: "only allows numbers" }
  validate :validate_jmbg_last_3_digits
  validate :validate_jmbg_date

  validates :gender, presence: { message: "gender can't be blank" }, inclusion: { in: %w(M F), message: "only allows M/F" }

  validates :email, presence: { message: "email can't be blank" }, uniqueness: { message: "email has to be unique" }, format: { with: URI::MailTo::EMAIL_REGEXP }

  validates :phone, presence: { message: "phone can't be blank" }, uniqueness: { message: "phone has to be unique" }, format: { with: /\A\+?[0-9]+\z/, message: "phone numbers has to be numbers with an optional + in the start" }

  validates :address, presence: { message: "address can't be blank" }

  validates :connected_accounts, presence: { message: "connected accounts can't be blank" }

  validates :active, presence: { message: "active can't be blank" }, inclusion: { in: [true, false] }

  has_secure_password

  validate :validate_password

  protected

  def password_required?
    super && (password.present? || password_confirmation.present?)
  end

  private

  def validate_jmbg_last_3_digits
    return unless gender.present?

    errors.add(:jmbg, "male jmbg ends with last 3 digits below 500") unless gender == "M" && jmbg[-3..-1].to_i < 500
    errors.add(:jmbg, "female jmbg ends with last 3 digits above 499") unless gender == "F" && jmbg[-3..-1].to_i > 499
  end

  def validate_jmbg_date
    return unless birth_date.present?

    birth_time = Time.at(birth_date / 1000)
    birth_year = birth_time.year % 1000
    jmbg_prefix = jmbg[0..6]
    errors.add(:jmbg, "JMBG and date do not match") unless jmbg_prefix == format("%02d02d03d", birth_time.day, birth_time.month, birth_year)
  end

  def validate_password
    return unless password.present?

    errors.add(:password, "password has to contain at least 1 small letter") unless password.match?(/(?=.*[a-z])/)
    errors.add(:password, "password has to contain at least 1 big letter") unless password.match?(/(?=.*[A-Z])/)
    errors.add(:password, "password has to contain at least 1 number") unless password.match?(/(?=.*\d)/)
    errors.add(:password, "password has to contain at least 1 special character") unless password.match?(/(?=.*[[:^alnum:]])/)
    errors.add(:password, "password has to have between 8 and 32 characters") unless password.length.between?(8, 32)
  end
end