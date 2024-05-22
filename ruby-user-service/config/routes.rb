Rails.application.routes.draw do
  namespace :api, path: "/api" do
    resources :favorite_users, only: [:index, :create, :update, :destroy]
    resources :payment_codes, only: [:index, :show]
    resources :one_time_passwords, only: [:create]
    resources :workers
    resources :users

    post "auth/login", to: "auth#login"
    post "auth/forgot_password", to: "auth#forgot_password"
    post "auth/reset_password", to: "auth#reset_password"

    post "users/register", to: "users#register"

    post "verification_codes/register", to: "verification_codes#create_register_code"
    post "verification_codes/reset", to: "verification_codes#create_reset_code"

    post "one_time_passwords/check", to: "one_time_passwords#check"

  end
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  # get "up" => "rails/health#show", as: :rails_health_check

  # Defines the root path route ("/")
  # root "posts#index"
end
