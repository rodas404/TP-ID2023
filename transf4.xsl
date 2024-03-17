<?xml version="1.0" encoding="UTF-8"?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Sat May 22 17:58:30 WEST 2021 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />


<xsl:template match="global">
		<html>
			<body>
			  <xsl:apply-templates select="escritor">
			  </xsl:apply-templates>
			</body>
		</html>
</xsl:template>

  <xsl:template match="escritor">
	<h2>
		Listagem de Obras do escritor <xsl:value-of select="@nome" />
	</h2>
	<table border="1">
		<tr>
			<th>Fotografia</th>
			<th>Título</th>
		</tr>
		<xsl:apply-templates select="livros/livro">
		  </xsl:apply-templates>
	</table>
  </xsl:template>
  
  <xsl:template match="livros/livro">
      <tr>
		<td>
			<img src="{capa}" width="100"/>
		</td>
		<td>
			<xsl:value-of select="titulo" />
		</td>
	</tr>
  </xsl:template>

</xsl:stylesheet>
