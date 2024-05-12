class CreateVerificationCodes < ActiveRecord::Migration[7.1]
  def change
    create_table :verification_codes do |t|
      t.string :email
      t.string :code
      t.Bigint :expiration
      t.boolean :reset

      t.timestamps
    end
  end
end
