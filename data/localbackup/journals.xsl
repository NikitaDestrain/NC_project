<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="journalContainer">
        <xsl:apply-templates select="//journals"/>
        <xsl:apply-templates select="//tasks"/>
    </xsl:template>

    <xsl:template match="journals">
        <table>
            <xsl:attribute name="class">
                <xsl:text>table</xsl:text>
            </xsl:attribute>
            <caption>
                <strong>JOURNALS</strong>
            </caption>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
            </tr>
            <xsl:for-each select="journal">
                <tr>
                    <td>
                        <xsl:value-of select="id/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="name/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="description/text()"/>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="tasks">
        <table>
            <xsl:attribute name="class">
                <xsl:text>table</xsl:text>
            </xsl:attribute>
            <caption>
                <strong>TASKS</strong>
            </caption>
            <tr>
                <th>ID</th>
                <th>Journal ID</th>
                <th>Status</th>
                <th>Name</th>
                <th>Description</th>
                <th>Planned date</th>
                <th>Notification date</th>
                <th>Upload date</th>
                <th>Change date</th>
            </tr>
            <xsl:for-each select="task">
                <tr>
                    <td>
                        <xsl:value-of select="id/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="journalId/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="status/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="name/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="description/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="plannedDate/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="notificationDate/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="uploadDate/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="changeDate/text()"/>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>