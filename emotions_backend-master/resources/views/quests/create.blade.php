<!Doctype HTML>
<html>

<body>

  <head>
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



    <link rel="apple-touch-icon" sizes="57x57" href="../resources/views/favicon.ico/apple-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="../resources/views/favicon.ico/apple-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="../resources/views/favicon.ico/apple-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="../resources/views/favicon.ico/apple-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="../resources/views/favicon.ico/apple-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="../resources/views/favicon.ico/apple-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="../resources/views/favicon.ico/apple-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="../resources/views/favicon.ico/apple-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="../resources/views/favicon.ico/apple-icon-180x180.png">
    <link rel="icon" type="image/png" sizes="192x192" href="../resources/views/favicon.ico/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="../resources/views/favicon.ico/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="../resources/views/favicon.ico/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="../resources/views/favicon.ico/favicon-16x16.png">
    <link rel="manifest" href="../resources/views/favicon.ico/manifest.json">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="../resources/views/favicon.ico/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
  </head>
  <div class="row">
    <div class="col-sm-8 offset-sm-2">
      <h1 class="display-3">Quest hinzufügen</h1>
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
        <form method="post" action="{{ route('quests.store') }}">
          @csrf
          <div class="form-group">
            <label for="Name">Name:</label>
            <input type="text" class="form-control" name="Name" value="{{ old('Name') }}" />
          </div>

          <!--
          <div class="form-group">
            <label for="target_id_select">zugeordnetes Ziel</label>
            <select class="form-control" id="target_id_select" name="target_id">
            </select>
          </div>
          -->

          <div class="form-group">
            <label for="point_id_select">zugeordneter Punkt</label>
            <select class="form-control" id="point_id_select" name="point_id">
            </select>
          </div>
          <button type="submit" class="btn btn-primary">Quest hinzufügen</button>
        </form>
      </div>
    </div>
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

/*

    const fetchPromiseTarget = fetch("http://localhost:80/Bachelorarbeit/public/api/auth/target", {
      "method": 'GET',
      "headers": {
        "Content-Type": "application/x-www-form-urlencoded",
        "X-Requested-With": "XMLHttpRequest",
        "Authorization": "Bearer " + access_token_bearer
      }

    });
    fetchPromiseTarget.then(response => {
      return response.json();
    }).then(points => {
      console.log(points);
      var x = document.getElementById("target_id_select");
      for (let i = 0; i < points.length; i++) {
        var option = document.createElement("option");
        option.text = points[i].id;
        x.add(option);
      }
    });
*/
  </script>
  </div>
  </div>
</body>

</html>