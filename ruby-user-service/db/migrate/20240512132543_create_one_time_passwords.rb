class CreateOneTimePasswords < ActiveRecord::Migration[7.1]
  def change
    create_table :one_time_passwords do |t|
      t.string :email
      t.string :password_digest
      t.integer :expiration

      t.timestamps
    end
  end
end
