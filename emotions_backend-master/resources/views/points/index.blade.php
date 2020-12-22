<!Doctype HTML>
<html>

<head>
  <link href="{{ URL::asset('../resources/sass/menu.scss') }}" rel="stylesheet" type="text/css">

  <link rel="stylesheet"
    href="https://unpkg.com/bootstrap-material-design@4.1.1/dist/css/bootstrap-material-design.min.css"
    integrity="sha384-wXznGJNEXNG1NFsbm0ugrLFMQPWswR3lds2VeinahP8N0zJw9VWSopbjv2x7WCvX" crossorigin="anonymous">

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
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
    integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
    crossorigin="" />
  <!-- Make sure you put this AFTER Leaflet's CSS -->
  <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"
    integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew=="
    crossorigin=""></script>

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
  <title>Punkte</title>
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

<body id="body">
    <div class="nav-right visible-xs">
      <div class="button" id="btn">
        <div class="bar top"></div>
        <div class="bar middle"></div>
        <div class="bar bottom"></div>
      </div>
    </div>
    <!-- nav-right -->
    <main class="main">
      <nav>
        <div class="nav-right hidden-xs">
          <div class="button" id="btn">
            <div class="bar top"></div>
            <div class="bar middle"></div>
            <div class="bar bottom"></div>
          </div>
        </div>
        <!-- nav-right -->
      </nav>
      <div class="loading_container" id="loading_container">
        <div class="loading">
          <div class="dot"></div>
          <div class="dot"></div>
          <div class="dot"></div>
          <div class="dot"></div>
          <div class="dot"></div>
        </div>
      </div>
      <div class="point_container" id="point_container">
        <div class="row">
          <div class="offset-sm-1 col-sm-10 offset-sm-1 offset-lg-1 col-lg-10 offset-lg-1">
            <div>
              <h1 class="display-3">Punkte</h1>
              <h2 class="display-5">Sollten nicht mehr als 100 Punkte sein, da Limitierung von Google auf der Karte in der APP</h2>
              <a href="{{ route('points.create')}}" class="btn btn-primary">Punkt hinzufügen</a>
              <br></br>
              <table style="width: 70vw; max-width: 70vw"class="table table-striped">
                <thead>
                  <tr>
                    <td>ID</td>
                    <td>Name</td>
                    <td>Text</td>
                    <td>Punkte</td>
                    <td>Längengrad</td>
                    <td>Breitengrad</td>
                    <td colspan=4>Aktionen</td>
                  </tr>
                </thead>
                <tbody>
                  @forelse($points as $point)
                  <tr>
                    <td>{{$point->id}}</td>
                    <td>{{$point->name}} </td>
                    <td style ="word-break:break-all;">{{$point->text}}</td>
                    <td>{{$point->QRnumber}}</td>
                    <td>{{$point->longitude}}</td>
                    <td>{{$point->latitude}}</td>
                    <td>
                      <a href="{{ route('points.show',$point->id)}}" class="btn btn-primary">BoxPlot</a>
                    </td>
                    <td>
                      <a href="{{ url('/showqr',$point->id)}}" class="btn btn-primary">QR Code</a>
                    </td>
                    <td>
                      <a href="{{ route('points.edit',$point->id)}}" class="btn btn-primary">Bearbeiten</a>
                    </td>
                    <td>
                      <a href="#" class="btn btn-danger" data-val="Punkt {{$point->name}} wirklich löschen?"
                        data-toggle="modal" data-target="#my-modal">Löschen</a>
                    </td>
                  </tr>
                  @empty
                  <p>Keine Punkte</p>
                  @endforelse
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="row" style="margin-top: 25px; margin-bottom: 25px;">
        </div>
        <div class="row">
          <div class="offset-sm-1 col-sm-10 offset-sm-1 offset-lg-1 col-lg-10 offset-lg-1">
            <div id="mapid" style="height: 480px; margin-bottom:25px;"></div>
            <script>
              document.getElementById('loading_container').style.visibility = "visible";
              document.getElementById('point_container').style.visibility = "hidden";
              document.getElementById("body").style.overflow = "hidden";
              if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(geoSuccess);
              } else {
                alert("Geolocation is not supported by this browser.");
              }

              function geoSuccess(position) {
                var lat = position.coords.latitude;
                var lng = position.coords.longitude;
                
                var mymap = L.map('mapid').setView([lat, lng], 13);
                L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={token}', {
                  maxZoom: 15,
                  id: 'mapbox/streets-v11',
                  tileSize: 512,
                  zoomOffset: -1,
                  token: 'pk.eyJ1IjoiYmFjaGVsb3JhcmJlb3QiLCJhIjoiY2tkOHB1eHFvMmUwMTJ0bnRnMHpoNmVpMyJ9.xQedn9fGEL6Can_Od4Ef-Q'
                }).addTo(mymap);

                //Get the access token for the API 
                const access_token_bearer = document.cookie
                  .split('; ')
                  .find(row => row.startsWith('access_token_bearer'))
                  .split('=')[1];

                console.log(access_token_bearer);

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
                  $('#my-modal').on('show.bs.modal', function (event) {
                    var myVal = $(event.relatedTarget).data('val');
                    $(this).find(".modal-body").text(myVal);
                  });
                  points.forEach(element => L.circle([element.latitude, element.longitude], {
                    color: '#bab4b4',
                    fillOpacity: 0.5,
                    radius: 250
                  }).bindPopup("<b>" + element.name + "</b><br>" + element.text).openPopup().addTo(mymap));
                }).then(() => {
                  document.getElementById("body").style.overflow = "visible";
                  document.getElementById("loading_container").style.width = "0px";
                  document.getElementById("loading_container").style.height = "0px";
                  document.getElementById('loading_container').style.visibility = "hidden";
                  document.getElementById('point_container').style.visibility = "visible";
                });
              }
            </script>
          </div>
        </div>
      </div>
    </main>
    <div class="sidebar">
      <ul class="sidebar-list">
        <li class="sidebar-item"><a href="{{ url('/points') }}" class="sidebar-anchor">Punkte</a></li>
        <li class="sidebar-item"><a href="{{ url('/questions') }}" class="sidebar-anchor">Fragen</a></li>
        <li class="sidebar-item"><a href="{{ url('/quests') }}" class="sidebar-anchor">Quests</a></li>
        <li class="sidebar-item" onclick="logout()"><a href="{{ url('/logout')}}" class="sidebar-anchor">logout</a></li>
        <script>
          function logout() {
            document.cookie = "access_token_bearer=;"
          }
        </script>
      </ul>
    </div>

    <div class="modal fade" id="my-modal" tabindex="-1" role="dialog" aria-labelledby="my-modal" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Löschen bestätigen</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
          </div>
          <div class="modal-footer">
            @isset($point)
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Schließen</button>
            <form action="{{ route('points.destroy', $point->id)}}" method="post">
              @csrf
              @method('DELETE')
              <button class="btn btn-danger" type="submit">Löschen</button>
            </form>
            @endisset
          </div>
        </div>
      </div>
    </div>
</body>

</html>