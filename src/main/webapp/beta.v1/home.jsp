<% System.out.println("Home"); %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
			
		<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		
    <link rel="icon" href="../img/favicon.ico">

    <title>Smart Panel</title>

    <!-- Bootstrap core CSS -->
    <link href="/beta/css/bootstrap/bootstrap.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <!-- link href="assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet"-->

    <!-- Custom styles for this template -->
    <!-- link href="navbar.css" rel="stylesheet"-->

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <!-- script src="assets/js/ie-emulation-modes-warning.js"></script-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body ng-app="ew" onload="">

		<!-- Menu view TODO -->
		<div ng-include src="'/beta/components/header/header.html'"></div>
		
		<div class="container"> 
			<!-- Main view -->
			<div ng-view></div>
			
			<!-- Footer view TODO -->
				<!--pre>project = {{project | json}}</pre>
				<pre>projects = {{projects | json}}</pre-->
    </div> <!-- /container -->

	
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		
		<!-- Angular -->
		<!--script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.8/angular.min.js"></script>
		<script src="https://code.angularjs.org/1.5.9/angular-route.min.js"></script>-->
		<script src="/beta/js/angular.min.js"></script>
		<script src="/beta/js/angular-route.min.js"></script>
			
		<!-- Modules -->
		<script src="/beta/app.js"></script>
		
		<script src="/beta/components/header/header-service.js"></script>
		<script src="/beta/components/monitor/monitor-service.js"></script>
		<script src="/beta/components/project/project-service.js"></script>
		<script src="/beta/components/device/device-service.js"></script>
		
		<script src="/beta/components/header/header.js"></script>
		<script src="/beta/components/monitor/monitor.js"></script>
		<script src="/beta/components/project/project.js"></script>
		<script src="/beta/components/device/device.js"></script>
		
  </body>
</html>
