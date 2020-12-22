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
    <!-- Plotly.js -->
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>

    <link rel="stylesheet" href="https://unpkg.com/bootstrap-table@1.17.1/dist/bootstrap-table.min.css">
    <script src="https://unpkg.com/bootstrap-table@1.17.1/dist/bootstrap-table.min.js"></script>

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
    @isset($point)
    <title>Punkt {{$point->id}} BoxPlot</title>
    @endisset
  </head>
  <div class="row">
    <div class="col-sm-8 offset-sm-2">
      @isset($point)
      <h1 class="display-3">Punkt {{$point->id}} BoxPlot</h1>
      @endisset
      <h3>Skala von --- => -3</h3>
      <h3>Skala bis +++ => +3</h3>
      <div id="myDiv"></div>
      <style>
        .modebar {
          display: none !important;
        }
      </style>
      <script>
        //Get the access token for the API 
        const access_token_bearer = document.cookie
          .split('; ')
          .find(row => row.startsWith('access_token_bearer'))
          .split('=')[1];

        const fetchPromise = fetch("http://localhost:80/Bachelorarbeit/public/api/auth/answer/alltoQuestion/" + '{{$point->id}}', {
          "method": 'GET',
          "headers": {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-Requested-With": "XMLHttpRequest",
            "Authorization": "Bearer " + access_token_bearer
          }

        });
        fetchPromise.then(response => {
          return response.json();
        }).then(questions => {
          var data = [];
          var data2 = [];
          console.log(questions);

          for (let i = 0; i < questions.length; i++) {
            for (let j = 0; j < questions[i].answers.length; j++) {
              questions[i].answers[j]['QuestionName'] = questions[i].Text;
              data2.push(questions[i].answers[j]);
            }
          }

          console.log(data2);
          $(document).ready(function () {
            // Use the given data to create  
            // the table and display it 
            $('table').bootstrapTable({
              data: data2
            });
          });

          for (let i = 0; i < questions.length; i++) {
            data.push(questions[i].Emotion = {
              x: [],
              type: 'box',
              name: questions[i].Emotion
            });
            for (let j = 0; j < questions[i].answers.length; j++) {
              questions[i].Emotion.x.push(questions[i].answers[j].Text);
            }
          }

          var layout = {
            title: 'Emotions Box Plot'
          };


          Plotly.newPlot('myDiv', data, layout);
        });
      </script>
    </div>
  </div>

  <div class="row"style="margin-bottom:25px" >
    <div class="col-sm-8 offset-sm-2">
      <div>
        <h1 class="display-3">Textfeld Antworten</h1>
        <br></br>
        <table class="table table-striped">
          <thead>
            <tr>
            <th data-field="id">
                <span class="text-success">
                  ID
                </span>
              </th>
              <th data-field="QuestionName">
                <span class="text-success">
                  Frage
                </span>
              </th>
              <th data-field="Text">
                <span class="text-success">
                  Wert
                </span>
              </th>
              <th data-field="TextFielText">
                <span class="text-success">
                  Antwort
                </span>
              </th>
              <th data-field="user_ID">
                <span class="text-success">
                  Nutzer
                </span>
              </th>
            </tr>
          </thead>
        </table>
      </div>
    </div>


</body>

</html>