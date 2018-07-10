<?php

    $email = $_GET['email'];

    $sql = new mysqli();

    $sql -> connect('localhost','zxlweb','123456','zxlweb');

    $sql -> set_charset('utf8');

    $res = $sql -> query("INSERT INTO `zxlweb`.`dongli` (`user`, `pwd`) VALUES ('".$us."', '".$pw."')");

    $row = $res -> fetch_row();

    echo json_encode($row);

?>