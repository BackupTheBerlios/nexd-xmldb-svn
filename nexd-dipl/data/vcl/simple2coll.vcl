<?xml version="1.0" encoding="iso-8859-1" ?>
<vcl:schema xmlns:vcl="http://nexd.xplib.de/vcl/version/1.0"
  name="simple2coll" postfix="vxml">

  <vcl:collection match="/db/articles">
    <articles>
      <vcl:variable name="author_id" select="//author/@id" />
      <title><vcl:value-of select="/info/title" /></title>
      <authors>
      <vcl:collection match="/db/authors" select="//id = $author_id">
        <author vcl:id="$authod_id">
          <vcl:value-of select="/author/name/*" />
        </author>
      </vcl:collection>
      </authors>
    </articles>
  </vcl:collection>
</vcl:schema>