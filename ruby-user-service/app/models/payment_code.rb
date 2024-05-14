class PaymentCode < ApplicationRecord
  validates :code, presence: { message: "form and basis can't be blank" }, numericality: { only_integer: true, greater_than_or_equal_to: 0, less_than_or_equal_to: 100, less_than: 1000, message: "must be between 100 and 999" }

  validates :payment_description, presence: { message: "payment description can't be blank" }
end
