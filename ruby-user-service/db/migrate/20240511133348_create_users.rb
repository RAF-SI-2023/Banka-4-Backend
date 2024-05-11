class CreateUsers < ActiveRecord::Migration[7.1]
  def change
    create_table :users do |t|
      t.string :first_name
      t.string :last_name
      t.string :jmbg
      t.string :birth_date
      t.string :gender
      t.string :email
      t.string :password_digest
      t.string :phone
      t.string :address
      t.string :connected_accounts
      t.boolean :active

      t.timestamps
    end
    add_index :users, :jmbg, unique: true
    add_index :users, :email, unique: true
  end
end
