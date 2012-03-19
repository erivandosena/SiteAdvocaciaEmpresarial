<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="refresh" content="70; URL=index.jsp" />
<title>
Página inicial Quercus&trade;
</title>
<meta name="robots" content="noindex,nofollow" />

<!--
<?php

  function quercus_test()
  {
    return function_exists("quercus_version");
  }

?>
-->

<style type="text/css">
.message {
  margin: 10px;
  padding: 10px;
  border: 1px solid blue;
  background: #CCCCCC;
}

.footer {
  font-size: small;
  font-style: italic;
}

#failure {
    <?php echo "display: none;"; ?> 
}

#failure_default_interpreter {
    display: none;
    <?php if (! quercus_test()) echo "display: block;"; ?> 
}

#success_pro {
    display: none;
    <?php if (quercus_is_pro() && quercus_test()) echo "display: block;"; ?> 
}

#success_open_source {
    display: none;
    <?php if (! quercus_is_pro() && quercus_test()) echo "display: block;"; ?> 
}
</style>
</head>

<body onLoad="document.getElementById('meuiframe').contentDocument.designMode='on'">
<a href="http://www.caucho.com"><img border="0" src="images/caucho-white.jpg" /></a>

<p>
Testes para Quercus&trade;...
</p>

<div class="message" id="failure">
Arquivos PHP não estão sendo interpretados por Quercus&trade;.
</div>

<div class="message" id="failure_default_interpreter">
PHP está sendo interpretado, mas não por Quercus&trade;!  Por favor, verifique sua configuração.
</div>

<div class="message" id="success_pro">
<img src="images/dragonfly-tiny.png" />Parabéns!  Quercus&trade; <?php if (quercus_test()) echo quercus_version(); ?> está compilando páginas PHP. Divirta-se!
</div>

<div class="message" id="success_open_source">
<img src="images/dragonfly-tiny.png" />Parabéns!  Quercus&trade; <?php if (quercus_test()) echo quercus_version(); ?> está interpretando as páginas PHP. Divirta-se!
</div>

<div>
A documentação está disponível em <a href="http://www.caucho.com">http://www.caucho.com</a>
</div>

<div>
O LEIAME está disponível <a href="README">aqui</a>.
</div>

<hr/>

<div class="footer">
Copyright &copy; 1998-2009
<a href="http://www.caucho.com">Caucho Technology, Inc</a>. 
All rights reserved.<br/>

Resin <sup><font size="-1">&#174;</font></sup> é uma marca registrada,
e Quercus<sup>tm</sup>, Amber<sup>tm</sup>, e Hessian<sup>tm</sup>
são marcas comerciais da Caucho Technology.
</div>

<?php phpinfo(); 
echo "Outros Testes, By Erivando:<br />";

echo $_SERVER['DOCUMENT_ROOT']. "ckfinder/userfiles/<br />";

$texto = $_SERVER['DOCUMENT_ROOT'];
$posicao = strpos($texto, 'webapps');
$saida = substr($texto, $posicao + 7);

echo $$baseUrl = "http://" . $_SERVER['HTTP_HOST'] . $saida . "ckfinder/userfiles/ <br />";

echo $_SERVER['SERVER_NAME']. "<br />";

?>

</body>

</html>