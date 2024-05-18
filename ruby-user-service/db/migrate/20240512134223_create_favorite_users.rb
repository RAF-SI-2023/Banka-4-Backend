class CreateFavoriteUsers < ActiveRecord::Migration[7.1]
  def change
    create_table :favorite_users do |t|
      t.integer :user_id
      t.string :sender_account_number
      t.string :sender_name
      t.string :receiver_account_number
      t.integer :number
      t.string :payment_code

      t.timestamps
    end
  end
end
