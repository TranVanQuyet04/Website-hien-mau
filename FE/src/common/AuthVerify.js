
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split(".")[1]));
  } catch (e) {
    return null;
  }
};

const AuthVerify = ({ logOut }) => {
  const navigate = useNavigate();

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
      const decodedJwt = parseJwt(user.accessToken);
      if (decodedJwt && decodedJwt.exp * 1000 < Date.now()) {
        logOut();
        navigate("/login");
      }
    }
  }, [logOut, navigate]);

  return null;
};

export default AuthVerify;
