export function getErrorMessage(error) {
  console.log(error)
  if (error.response) {
    if (error.response.status === 500) {
      return 'notification.500'
    } else if (error.response.status === 400) {
      return 'notification.400'
    } else return 'notification.generic_error'
  } else if (error.request) {
    return 'notification.network_error'
  } else {
    return 'notification.generic_error'
  }
}
