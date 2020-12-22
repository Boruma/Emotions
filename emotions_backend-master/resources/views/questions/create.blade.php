<!Doctype HTML>
<html>

<head>
  <link rel="stylesheet"
    href="https://unpkg.com/bootstrap-material-design@4.1.1/dist/css/bootstrap-material-design.min.css"
    integrity="sha384-wXznGJNEXNG1NFsbm0ugrLFMQPWswR3lds2VeinahP8N0zJw9VWSopbjv2x7WCvX" crossorigin="anonymous">
  <link href="{{ URL::asset('../resources/sass/menu.scss') }}" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
    integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
    integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
    crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
    integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
    crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
    integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
    crossorigin="anonymous"></script>
    
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-57x57.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-60x60.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-72x72.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-76x76.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-114x114.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-120x120.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-144x144.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-152x152.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/apple-icon-180x180.png')}}"
    type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-32x32.png')}}" type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-96x96.png')}}" type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/favicon-16x16.png')}}" type="image/x-icon">
  <link rel="shortcut icon" href="{{ asset('../resources/views/favicon.ico/android-icon-192x192.png')}}"
    type="image/x-icon">
  <link rel="manifest" href="../favicon.ico/manifest.json">
  <meta name="msapplication-TileColor" content="#ffffff">
  <meta name="msapplication-TileImage" content="../favicon.ico/ms-icon-144x144.png">
  <meta name="theme-color" content="#ffffff">
  <title>Frage erstellen</title>
</head>

<script>
  $(document).ready(function () {
    function toggleSidebar() {
      $(".button").toggleClass("active");
      $("main").toggleClass("move-to-left");
      $(".sidebar-item").toggleClass("active");
    }

    $(".button").on("click tap", function () {
      toggleSidebar();
    });

    $(document).keyup(function (e) {
      if (e.keyCode === 27) {
        toggleSidebar();
      }
    });
  });
</script>

<body>
  <div class="row">
    <div class="col-sm-8 offset-sm-2">
      <h1 class="display-3">Frage hinzufügen</h1>
      <div>
        @if ($errors->any())
        <div class="alert alert-danger">
          <ul>
            @foreach ($errors->all() as $error)
            <li>{{ $error }}</li>
            @endforeach
          </ul>
        </div><br />
        @endif
        <form method="post" action="{{ route('questions.store') }}">
          @csrf
          <div class="form-group">
            <label for="text">Text:</label>
            <input type="text" class="form-control" name="text" value="{{ old('text') }}" />
          </div>

          <div class="form-group">
            <label for="Emotion">Emotion:</label>
            <input type="Emotion" class="form-control" name="Emotion" value="{{ old('Emotion') }}" />
          </div>

          <div class="form-group">
            <label for="point_id_select">zugeordneter Punkt</label>
            <select class="form-control" id="point_id_select" name="point_id">
            </select>

            <div class="form-group">
              <label for="TimeActive">Zeitraum</label>
              <select class="form-control" id="TimeActive" name="TimeActive">
                <option>0-8</option>
                <option>8-16</option>
                <option>16-24</option>
              </select>
            </div>
            <script>
              //Get the access token for the API 
              const access_token_bearer = document.cookie
                .split('; ')
                .find(row => row.startsWith('access_token_bearer'))
                .split('=')[1];

              const fetchPromise = fetch("http://localhost:80/Bachelorarbeit/public/api/auth/points", {
                "method": 'GET',
                "headers": {
                  "Content-Type": "application/x-www-form-urlencoded",
                  "X-Requested-With": "XMLHttpRequest",
                  "Authorization": "Bearer " + access_token_bearer
                }

              });
              fetchPromise.then(response => {
                return response.json();
              }).then(points => {
                console.log(points);
                var x = document.getElementById("point_id_select");
                for (let i = 0; i < points.length; i++) {
                  var option = document.createElement("option");
                  option.text = points[i].id;
                  x.add(option);
                }
              });
            </script>

            <button type="submit" class="btn btn-primary">Punkt hinzufügen</button>
        </form>
      </div>
    </div>
  </div>
</body>

</html>