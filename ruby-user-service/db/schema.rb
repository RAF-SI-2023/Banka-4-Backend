# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# This file is the source Rails uses to define your schema when running `bin/rails
# db:schema:load`. When creating a new database, `bin/rails db:schema:load` tends to
# be faster and is potentially less error prone than running all of your
# migrations from scratch. Old migrations may fail to apply correctly if those
# migrations use external dependencies or application code.
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema[7.1].define(version: 2024_05_12_134223) do
  create_table "favorite_users", force: :cascade do |t|
    t.integer "userId"
    t.string "sender_account_number"
    t.string "sender_name"
    t.string "receiver_account_number"
    t.integer "number"
    t.string "payment_code"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "one_time_passwords", force: :cascade do |t|
    t.string "email"
    t.string "password_digest"
    t.integer "expiration"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "payment_codes", force: :cascade do |t|
    t.integer "form_and_basis"
    t.string "payment_description"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "users", force: :cascade do |t|
    t.string "first_name"
    t.string "last_name"
    t.string "jmbg"
    t.integer "birth_date"
    t.string "gender"
    t.string "email"
    t.string "password_digest"
    t.string "phone"
    t.string "address"
    t.string "connected_accounts"
    t.boolean "active"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "encrypted_password", default: "", null: false
    t.string "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.index ["email"], name: "index_users_on_email", unique: true
    t.index ["jmbg"], name: "index_users_on_jmbg", unique: true
    t.index ["phone"], name: "index_users_on_phone", unique: true
    t.index ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true
  end

  create_table "verification_codes", force: :cascade do |t|
    t.string "email"
    t.string "code"
    t.integer "expiration"
    t.boolean "reset"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "workers", force: :cascade do |t|
    t.string "first_name"
    t.string "last_name"
    t.string "jmbg"
    t.integer "birth_date"
    t.string "gender"
    t.string "email"
    t.string "password_digest"
    t.string "phone"
    t.string "address"
    t.string "username"
    t.string "position"
    t.string "department"
    t.string "string"
    t.integer "permission"
    t.boolean "active"
    t.integer "firmId"
    t.decimal "daily_limit"
    t.decimal "daily_spent"
    t.boolean "approval_flag"
    t.boolean "supervisor"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "encrypted_password", default: "", null: false
    t.string "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.index ["email"], name: "index_workers_on_email", unique: true
    t.index ["jmbg"], name: "index_workers_on_jmbg", unique: true
    t.index ["phone"], name: "index_workers_on_phone", unique: true
    t.index ["reset_password_token"], name: "index_workers_on_reset_password_token", unique: true
  end

end
