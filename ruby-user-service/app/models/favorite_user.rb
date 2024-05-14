class FavoriteUser < ApplicationRecord
  validates :user_id, presence: { message: "user id can't be blank" }

  validates :sender_name, presence: { message: "sender name can't be blank" }

  validates :sender_account_number, presence: { message: "sender id can't be blank" }, length: { is: 18 }, format: { with: /\A\d{18}\z/, message: "sender account number must be 18 digits" }

  validates :receiver_account_number, presence: { message: "receiver id can't be blank" }, length: { is: 18 }, format: { with: /\A\d{18}\z/, message: "receiver account number must be 18 digits" }

  validates :number, presence: { message: "number can't be blank" }

  validates :payment_code, presence: { message: "payment code can't be blank" }
end
