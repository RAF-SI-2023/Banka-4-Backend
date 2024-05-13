class CreateWorkers < ActiveRecord::Migration[7.1]
  def change
    create_table :workers do |t|
      t.string :first_name
      t.string :last_name
      t.string :jmbg
      t.integer :birth_date
      t.string :gender
      t.string :email
      t.string :password_digest
      t.string :phone
      t.string :address
      t.string :username
      t.string :position
      t.string :department
      t.string :string
      t.integer :permission
      t.boolean :active
      t.integer :firmId
      t.decimal :daily_limit
      t.decimal :daily_spent
      t.boolean :approval_flag
      t.boolean :supervisor

      t.timestamps
    end
    add_index :workers, :jmbg, unique: true
    add_index :workers, :email, unique: true
    add_index :workers, :phone, unique: true
  end
end
