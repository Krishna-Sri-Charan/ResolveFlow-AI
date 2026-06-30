import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

function CommonSnackbar({
  notification,
  setNotification,
  autoHideDuration = 3500,
  anchorOrigin = {
    vertical: "top",
    horizontal: "right",
  },
}) {
  return (
    <Snackbar
      open={notification.open}
      autoHideDuration={autoHideDuration}
      anchorOrigin={anchorOrigin}
      onClose={() =>
        setNotification((prev) => ({
          ...prev,
          open: false,
        }))
      }
    >
      <Alert
        severity={notification.severity}
        variant="filled"
        sx={{ width: "100%" }}
        onClose={() =>
          setNotification((prev) => ({
            ...prev,
            open: false,
          }))
        }
      >
        {notification.message}
      </Alert>
    </Snackbar>
  );
}

export default CommonSnackbar;