xquery version "1.0";

<resultados>
{
  for $x in doc("global.xml")/global/escritor
  
  return
    ("&#10;", "Nome: ", $x/nomeCompleto/text())
}
</resultados>

