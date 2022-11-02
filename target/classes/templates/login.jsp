<!DOCTYPE HTML>
<html lang="en">
<head>
<title>Wint Google Demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="/js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/css/app.css" />
<style>
div {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}
</style>
</head>

<body>
	<div>
		<button type="button" class="btn btn-light" onclick="handleLoginClick()">
			<img alt="" width="26px" height="auto"
				src="data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' viewBox='0 0 48 48'%3e%3cdefs%3e%3cpath id='a' d='M44.5 20H24v8.5h11.8C34.7 33.9 30.1 37 24 37c-7.2 0-13-5.8-13-13s5.8-13 13-13c3.1 0 5.9 1.1 8.1 2.9l6.4-6.4C34.6 4.1 29.6 2 24 2 11.8 2 2 11.8 2 24s9.8 22 22 22c11 0 21-8 21-22 0-1.3-.2-2.7-.5-4z'/%3e%3c/defs%3e%3cclipPath id='b'%3e%3cuse xlink:href='%23a' overflow='visible'/%3e%3c/clipPath%3e%3cpath clip-path='url(%23b)' fill='%23FBBC05' d='M0 37V11l17 13z'/%3e%3cpath clip-path='url(%23b)' fill='%23EA4335' d='M0 11l17 13 7-6.1L48 14V0H0z'/%3e%3cpath clip-path='url(%23b)' fill='%2334A853' d='M0 37l30-23 7.9 1L48 0v48H0z'/%3e%3cpath clip-path='url(%23b)' fill='%234285F4' d='M48 48L17 24l-4-3 35-10z'/%3e%3c/svg%3e" />
			Login with Google</button>
	</div>
	<script type="text/javascript">
      const CLIENT_ID = '[[${gApiClientId}]]';
      const SCOPES = 'email profile https://www.googleapis.com/auth/photoslibrary';

      function gisLoaded() {
         tokenClient = google.accounts.oauth2.initTokenClient({
            client_id: CLIENT_ID,
            scope: SCOPES,
         });
      }

      function handleLoginClick() {
         tokenClient.callback = async (resp) => {
            if (resp.error !== undefined) {
               throw (resp);
            }
            const accessToken = resp && resp.access_token;
            if (accessToken) {
            	const form = document.createElement('form');
            	form.action = 'login';
            	form.method = 'POST';
            	form.enctype = 'multipart/form-data';
            	
            	const input = document.createElement('input');
            	input.type = 'hidden';
            	input.name = 'accessToken';
            	input.value = accessToken;
            	
            	form.appendChild(input);
            	document.body.appendChild(form);
            	form.submit();
            }
         };
         tokenClient.requestAccessToken();
      }

      function handleLogoutClick() {
         const token = gapi.client.getToken();
         if (token !== null) {
            google.accounts.oauth2.revoke(token.access_token);
            gapi.client.setToken('');
         }
      }
   </script>
	<script async defer src="https://accounts.google.com/gsi/client"
		onload="gisLoaded()"></script>
</body>

</html>