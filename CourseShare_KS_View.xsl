<?xml version="1.0" encoding="euc-kr" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
		<html>
			<head>
				<title>Personalized Knowledge Structure - Analysis Results</title>
				<style type="text/css">
					h4 {
						font-style : "italic";
						font-weight : "bold";
						color : "rgb(30, 50, 255)";
						font-family : "georgia"
					}
				</style>
			</head>
			<body bgcolor="#FFFF99">
				<p align="center"><font color="##009966" size="6"> *** Personalized Knowledge Structure - Analysis Results *** </font></p>
				<br />
				<table border="1" cellpadding="5" cellspacing="2" align="center">
					<THEAD>
						<tr align="center">
							<!-- 1. Case Number -->
							<th bgcolor="#006666"><font color="#FFFFFF">Case Number</font></th>
							<!-- 2. KS Map Generated Date & Time -->
							<th bgcolor="#006666"><font color="#FFFFFF">KS Map Generated Date &amp; Time</font></th>
							<!-- 3. No. of User Selected Terms  -->
							<th bgcolor="#006666"><font color="#FFFFFF">No. of User Selected Terms</font></th>
							<!-- 4. Processing Time to Generate KS Map -->
							<th bgcolor="#006666"><font color="#FFFFFF">Processing Time to Generate KS Map</font></th>
							<!-- 5. No. of Nodes -->
							<th bgcolor="#006666"><font color="#FFFFFF">No. of Nodes </font></th>
							<!-- 6. No. of Links -->
							<th bgcolor="#006666"><font color="#FFFFFF">No. of Links</font></th>
							<!-- 7. Coherence -->
							<th bgcolor="#006666"><font color="#FFFFFF">Coherence</font></th>
							<!-- 8. Weight Information -->
							<th bgcolor="#006666"><font color="#FFFFFF">Weight Information</font></th>
							<!-- 9. File Name w/ Extension -->
							<th bgcolor="#006666"><font color="#FFFFFF">File Name w/ Extension</font></th>
							<!-- 10. File Uploaded Date & Time -->
							<th bgcolor="#006666"><font color="#FFFFFF">File Uploaded Date &amp; Time</font></th>
							<!-- 11. Total No. of Words -->
							<th bgcolor="#006666"><font color="#FFFFFF">Total No. of Words</font></th>
							<!-- 12. No. of Auto Extracted Terms -->
							<th bgcolor="#006666"><font color="#FFFFFF">No. of Auto Extracted Terms</font></th>
							<!-- 13. Image of KS Map -->
							<th bgcolor="#006666"><font color="#FFFFFF">Image of KS Map</font></th>
						</tr>
					</THEAD>
					<TBODY>
						<xsl:apply-templates select="KSRoot" />		
					</TBODY>
				</table>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="KSRoot">
		<xsl:for-each select="./KSInfo">
			<tr align="center">
				<!-- 1. Case Number -->
				<td><xsl:value-of select="./CaseNumber" /></td>
				<!-- 2. KS Map Generated Date & Time -->
				<td><xsl:value-of select="./KSMapGeneratedDateTime" /></td>
				<!-- 3. No. of User Selected Terms  -->
				<td><xsl:value-of select="./NoOfUserSelectedTerms" /></td>
				<!-- 4. Processing Time to Generate KS Map -->
				<td><xsl:value-of select="./ProcessingTimeToGenerateKSMap" /></td>
				<!-- 5. No. of Nodes -->
				<td><xsl:value-of select="./NoOfNodes" /></td>
				<!-- 6. No. of Links -->
				<td><xsl:value-of select="./NoOfLinks" /></td>
				<!-- 7. Coherence -->
				<td><xsl:value-of select="./Coherence" /></td>
				<!-- 8. Weight Information -->
				<td><xsl:value-of select="./WeightInformation" /></td>
				<!-- 9. File Name w/ Extension -->
				<td><xsl:value-of select="./FileNameWithExtension" /></td>
				<!-- 10. File Uploaded Date & Time -->
				<td><xsl:value-of select="./FileUploadedDateTime" /></td>
				<!-- 11. Total No. of Words -->
				<td><xsl:value-of select="./TotalNoOfWords" /></td>
				<!-- 12. No. of Auto Extracted Terms -->
				<td><xsl:value-of select="./NoOfAutoExtractedTerms" /></td>
				<!-- 13. Image of KS Map -->
				<td><xsl:apply-templates select="./ImageOfKSMap" /></td>
			</tr>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="ImageOfKSMap">
		<xsl:element name="img">
			<xsl:attribute name="src">
				<xsl:value-of select="." />
			</xsl:attribute>

			<xsl:attribute name="width">500</xsl:attribute>
			<xsl:attribute name="height">500</xsl:attribute>
			<xsl:attribute name="alt">Image of KS Map</xsl:attribute>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
