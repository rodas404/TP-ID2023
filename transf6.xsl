<?xml version="1.0" encoding="UTF-8"?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Sat May 22 17:58:30 WEST 2021 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />


<xsl:template match="global">
	<html>
		<body>
			<h2>
				Listagem de autores falecidos
			</h2>
			<table border="1">
			<tr>
				<th>Fotografia</th>
				<th>Nome</th>
				<th>Nome Completo</th>
				<th>Nacionalidade</th>
				<th>Falecimento</th>			
				<th>Local</th>			
			</tr>
			  <xsl:for-each select="escritor">
				<xsl:if test="morte/ano>0">
					<tr>
						<td>
							<img src="{fotografia}" width="100"/>
						</td>
						<td>
							<xsl:value-of select="@nome" />
						</td>
						<td>
							<xsl:value-of select="nomeCompleto" />
						</td>
						<td>
							<xsl:value-of select="nacionalidade" />
						</td>
						<td>
							<xsl:value-of select="morte/ano" />
						</td>
						<td>
							<xsl:value-of select="morte/local" />
						</td>
					</tr>
				</xsl:if >  					
			</xsl:for-each > 
		</table>
		</body>
	</html>
</xsl:template>

</xsl:stylesheet>
