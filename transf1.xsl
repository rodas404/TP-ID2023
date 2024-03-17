<?xml version="1.0" encoding="UTF-8"?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Sat May 22 17:58:30 WEST 2021 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />
	
	<xsl:template match="escritores">
		<html>
			<body>
				<table border="1">
					<h1>Listagem de Escritores</h1>
					<tr>
						<th>Fotografia</th>
						<th>Escritor</th>
					</tr>
					<xsl:apply-templates select="escritor" />
				</table>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="escritor">
		<tr>
			<td>
				<img src="{fotografia}" width="100"/>
			</td>
			<td>
				<xsl:value-of select="@nome" />
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>





