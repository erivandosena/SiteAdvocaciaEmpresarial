//Atualizado a opcao letra normal by Erivando 13-02-2011

var tagAlvo = new Array('p'); //pega todas as tags p//
var tamanhos = new Array('9px','10px','11px','12px','13px','14px','15px','16px','17px','18px','19px','20px','21px','22px','23px','24px');
var tamanhoInicial = 5;
 
function mudaTamanho( idAlvo,acao,normal ){
  if (!document.getElementById) return
  var selecionados = null,tamanho = tamanhoInicial,i,j,tagsAlvo;
  if (normal > 0)
  tamanho = 5;
  else
  tamanho += acao;
  if ( tamanho < 0 ) tamanho = 0;
  if ( tamanho > 16 ) tamanho = 16;
  tamanhoInicial = tamanho;
  if ( !( selecionados = document.getElementById( idAlvo ) ) ) selecionados = document.getElementsByTagName( idAlvo )[ 0 ];
  
  selecionados.style.fontSize = tamanhos[ tamanho ];
  
  for ( i = 0; i < tagAlvo.length; i++ ){
    tagsAlvo = selecionados.getElementsByTagName( tagAlvo[ i ] );
    for ( j = 0; j < tagsAlvo.length; j++ ) tagsAlvo[ j ].style.fontSize = tamanhos[ tamanho ];
  }
}
