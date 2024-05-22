class CreateOneTimePasswords < ActiveRecord::Migration[7.1]
  def change
    create_table :one_time_passwords do |t|
      t.string :email
      t.string :one_time_password
      t.integer :expiration

      t.timestamps
    end
  end
end
