class Api::WorkersController < ApplicationController
  require_relative '../../utils/permission_checker'
  require_relative '../../services/auth_service'

  before_action :set_worker, only: %i[ update destroy ]
  before_action :wrap_params, except: %i[ index destroy ]
  before_action :authenticate_user, only: %i[ index create update destroy ]

  # GET /workers
  def index
    return render_unauthorized unless check_permissions?([:list_workers])

    @workers = Worker.where(active: true)
    render json: @workers
  end

  # POST /workers
  def create
    return render_unauthorized unless check_permissions?([:create_workers])

    @worker = Worker.new(create_worker_params)

    if @worker.valid? && @worker.save
      render json: @worker, status: :ok
    else
      render_bad_request
    end
  end

  # PATCH/PUT /workers/1
  def update
    return render_unauthorized unless check_permissions?([:edit_workers])

    if @worker.update(update_worker_params)
      render json: @worker
    else
      render_bad_request
    end
  end

  # DELETE /workers/1
  def destroy
    return render_unauthorized unless check_permissions?([:deactivate_workers])

    @worker.active = false
    if @worker.save
      render status: :ok
    else
      render_bad_request
    end
  end

  private

  # Use callbacks to share common setup or constraints between actions.
  def set_worker
    @worker = Worker.find(params[:id])
  end

  # Only allow a list of trusted parameters when creating a worker
  def create_worker_params
    params.require(:worker).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :password, :phone, :address, :username, :position, :department, :permissions, :active, :firm_id, :daily_limit, :approval_flag, :supervisor)
  end

  # Only allow a list of trusted parameters when creating a worker
  def update_worker_params
    params.require(:worker).permit(:id, :last_name, :password, :phone, :address, :position, :department, :permissions, :firm_id, :daily_limit, :approval_flag, :supervisor)
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request
    render json: @worker.errors, status: :bad_request
  end

  def authenticate_user
    authorization_header = request.headers['Authorization']
    return render_unauthorized unless authorization_header && authorization_header.start_with?('Bearer ')

    token = authorization_header.split(' ').last
    return render_unauthorized unless token

    @current_user = AuthService.authenticate_user(token)
    render_unauthorized unless @current_user
  end

  def check_permissions?(actions)
    unless @current_user && @current_user.respond_to?(:permissions)
      return false
    end

    PermissionsChecker.can_perform_actions?(@current_user.permissions, actions)
  end

  def wrap_params
    return if params[:worker]

    params[:worker] = params.permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :password, :phone, :address, :username, :position, :department, :permissions, :active, :firm_id, :daily_limit, :approval_flag, :supervisor)
  end
end
