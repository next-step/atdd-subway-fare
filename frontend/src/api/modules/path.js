import ApiService from '@/api'

const BASE_URL = '/paths'

const PathService = {
  get({ source, target, type, time }) {
    return ApiService.get(`${BASE_URL}/?source=${source}&target=${target}&type=${type}${time ? `&time=${time}` : ''}`)
  }
}

export default PathService
