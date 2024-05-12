class CreatePaymentCodes < ActiveRecord::Migration[7.1]
  def change
    create_table :payment_codes do |t|
      t.integer :form_and_basis
      t.string :payment_description

      t.timestamps
    end
  end
end
