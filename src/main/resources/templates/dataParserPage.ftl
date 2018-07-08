<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="/all/css/dataParserStyle.css" >
    <link rel="stylesheet" href="/all/css/custom.css">
</head>
<body>

<div style="width: 85%; margin: 0 auto;">

<div style="float: left;width: 24%; margin-top: 7%;">
    <form  method="post" action="/addCategory">
    <div class="form-group">
        <label for="categories"></label>
        <textarea name="categories" id="categories" class="form-control" rows="10" required></textarea>
    </div>
    <div class="form-group">
        <button type="submit" style="width: 100%;" class="btn btn-success">add categories</button>
    </div>
    </form>
</div>

<div class="dataContent"  style="float: right; width: 75%;">
<table id="myTable" class="table table-striped table-hover">
    <thead class="thead-light">
    <tr>
        <th scope="col">#</th>
        <th scope="col">Keyword</th>
        <th scope="col">Status</th>
        <th scope="col">States</th>
        <th scope="col">Action</th>
        <th scope="col">Action</th>
    </tr>
    </thead>
    <tbody>
    <#list model.tasks as dataParser>
    <tr class="${dataParser.category.title?replace(" ","")}">
        <th class="increment" scope="row"></th>
        <td>${dataParser.category.title}</td>
        <#if dataParser.isCompleted>
            <td>Done</td>
        <#else >
            <td  id="${dataParser.category.title?replace(" ","")}">Waiting</td>
        </#if>
        <#if dataParser.onlySelectedCities>
        <td>${dataParser.size}</td>
        <#else>
        <td> All</td>
        </#if>
        <#if dataParser.isCompleted>
            <td><button type="button" class="btn btn-danger" onclick="deleteData('${dataParser.category.title}')">Delete</button></td>
            <td><a href="/download/${dataParser.category.title}" class="btn btn-success">Download</a></td>
        <#else >
            <td><button type="button" class="btn btn-danger" disabled>Delete</button></td>
            <td><button type="button" class="btn btn-success" disabled>Download</button></td>
        </#if>
    </tr>
    <#else >
    </#list>
    </tbody>
</table>
</div>
</div>

<#--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>-->
<script src="/all/libs/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
<script src="//cdn.jsdelivr.net/sockjs/1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/all/js/websocketStomp.js"></script>

<script>

    $(document).ready( function () {
        $('#myTable').DataTable();
    } );

</script>
<script>
    function deleteData(data) {
        $.ajax({
            method: 'POST',
            url: '/delete/'+data,
            success: function() {
                $('.'+data).detach();
            }
        });
    }
</script>

</body>
</html>