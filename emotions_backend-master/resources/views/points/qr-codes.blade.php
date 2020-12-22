<!Doctype HTML>
<html>

<body style="overflow: hidden;">

  <head>
    <link href="{{ URL::asset('../resources/sass/menu.scss') }}" rel="stylesheet" type="text/css">

    <link rel="stylesheet" href="https://unpkg.com/bootstrap-material-design@4.1.1/dist/css/bootstrap-material-design.min.css" integrity="sha384-wXznGJNEXNG1NFsbm0ugrLFMQPWswR3lds2VeinahP8N0zJw9VWSopbjv2x7WCvX" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <!--jsPDF-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.debug.js" integrity="sha384-NaWTHo/8YCBYJ59830LTz/P4aQZK1sS0SneOgAvhsIl3zBu8r9RevNg5lHCHAuQ/" crossorigin="anonymous"></script>



    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-57x57.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-60x60.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-72x72.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-76x76.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-114x114.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-120x120.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-144x144.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-152x152.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-180x180.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-32x32.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-96x96.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-16x16.png')}}" type="image/x-icon">
    <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/android-icon-192x192.png')}}" type="image/x-icon">
    <link rel="manifest" href="../favicon.ico/manifest.json">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="../favicon.ico/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
    <title>Punkt {{$point->id}} QR-code</title>
  </head>
  <div class="row">
    <div class="col-sm-8 offset-sm-2">
      <h1 class="display-3">Punkt {{$point->id}} QR-code</h1>
      <a href="#" onclick="dowloadQR()" class="btn btn-primary">Download</a>
      <div class="row">
        <div class="col-sm-8 offset-sm-2">
          <img id="qr-code">
        </div>
      </div>
      <style>
        .modebar {
          display: none !important;
        }
      </style>
      <script>
        var doc = new jsPDF();
        //Get the access token for the API 
        const access_token_bearer = document.cookie
          .split('; ')
          .find(row => row.startsWith('access_token_bearer'))
          .split('=')[1];
        //get QR Code Image
        const fetchPromise = fetch("http://localhost:80/Bachelorarbeit/public/api/auth/store_image/fetch_image/toPoint/" + '{{$point->id}}', {
          "method": 'GET',
          "headers": {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-Requested-With": "XMLHttpRequest",
            "Authorization": "Bearer " + access_token_bearer
          }

        });
        //
        fetchPromise.then(response => {
          return response.blob();
        }).then(image => {
          var objectURL = URL.createObjectURL(image);
          var myImage = document.getElementById('qr-code');
          myImage.src = objectURL;

          //Convert BLOB TO BASE64
          var reader = new FileReader();
          reader.readAsDataURL(image);
          //Generate PDF
          reader.onloadend = function() {
            var base64String = reader.result;
            doc.setFontSize(24);
            doc.text("Punkt " + '{{$point->id}}', 10, 10);
            doc.text("Name " + '{{$point->name}}', 10, 20);
            doc.text("Text " + '{{$point->text}}', 10, 30);
            doc.text("Punkte " + '{{$point->QRnumber}}', 10, 40);
            doc.text("Longitude " + '{{$point->longitude}}', 10, 50);
            doc.text("Latitude " + '{{$point->latitude}}', 10, 60);
            doc.addImage(base64String, 'png', 10, 70, 180, 160);
          }
        });
        //Download the generated PDF
        function dowloadQR() {
          doc.save("Punkt " + '{{$point->id}}' + '.pdf');
        }
      </script>


</body>

</html>