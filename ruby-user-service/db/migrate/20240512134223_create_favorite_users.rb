class CreateFavoriteUsers < ActiveRecord::Migration[7.1]
  def change
    create_table :favorite_users do |t|
      t.Bigint :userId
      t.string :sender_account_number
      t.string :sender_name
      t.string :sender_account_number
      t.integer :number
      t.string :payment_code

      t.timestamps
    end
  end
end
