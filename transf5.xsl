<?xml version="1.0" encoding="UTF-8"?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Sat May 22 17:58:30 WEST 2021 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />
	
	<xsl:template match="global">
		<html>
			<body>
				<table border="1">
					<h1>Listagem de Escritores nascidos depois de 1950</h1>
					<tr>
						<th>Escritor</th>
						<th>Ano nascimento</th>
						<th>Nacionalidade</th>
					</tr>
					<xsl:for-each select="escritor">
						<xsl:if test="nascimento/ano>1950">
							<tr>
								<td>
									<xsl:value-of select="@nome" />
								</td>
								<td>
									<xsl:value-of select="nascimento/ano" />
								</td><td>
									<xsl:value-of select="nacionalidade" />
								</td>								
							</tr>
						</xsl:if >  					
					</xsl:for-each > 
				</table>
			</body>
		</html>
	</xsl:template>
	
</xsl:stylesheet>
