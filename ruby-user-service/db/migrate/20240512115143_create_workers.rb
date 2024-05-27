class CreateWorkers < ActiveRecord::Migration[7.1]
  def change
    create_table :workers do |t|
      t.string :first_name
      t.string :last_name
      t.string :jmbg
      t.string :email
      t.text :password_digest
      t.integer :birth_date
      t.string :gender
      t.string :phone
      t.string :address
      t.string :username
      t.string :position
      t.string :department
      t.integer :permissions
      t.boolean :active
      t.integer :firm_id
      t.decimal :daily_limit
      t.decimal :daily_spent
      t.boolean :approval_flag
      t.boolean :supervisor

      t.timestamps
    end
    #add_index :workers, :username, unique: true
    #add_index :workers, :email, unique: true
    #add_index :workers, :jmbg, unique: true
    #add_index :workers, :phone, unique: true
  end
end
