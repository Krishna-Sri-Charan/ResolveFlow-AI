export const getNotificationFromError = (
  error,
  fallbackMessage = "Something went wrong."
) => {

  const status = error.response?.status;

  return {
    severity: status === 429 ? "warning" : "error",
    message:
      error.response?.data?.message ||
      fallbackMessage
  };

};