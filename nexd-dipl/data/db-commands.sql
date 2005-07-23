SELECT
  doc.version AS name, doc.encoding AS val, doc.standalone AS alone, 
  NULL AS pid, NULL AS sid, n0.* 
FROM
  nexd_dom_document AS doc 
LEFT JOIN
  nexd_dom_node AS n0 ON n0.id=doc.nid 
WHERE
  n0.rid=1 
UNION SELECT
  elem.elemname AS name, NULL AS val, false AS alone, 
  NULL AS pid, NULL AS sid, n1.* 
FROM
  nexd_dom_element AS elem 
LEFT JOIN
  nexd_dom_node AS n1 ON n1.id=elem.nid 
WHERE
  n1.rid=1 
UNION SELECT  
  attr.attrname AS name, attr.attrvalue AS val, false AS alone, 
  NULL AS pid, NULL AS sid, n2.* 
FROM
  nexd_dom_attr AS attr 
LEFT JOIN
  nexd_dom_node AS n2 ON n2.id=attr.nid 
WHERE
  n2.rid=1 
UNION SELECT
  NULL AS name, txt.textvalue AS val, false AS alone, 
  NULL AS pid, NULL AS sid, n3.* 
FROM
  nexd_dom_text AS txt 
LEFT JOIN
  nexd_dom_node AS n3 ON n3.id=txt.nid 
WHERE
  n3.rid=1 
UNION SELECT
  ref.name, NULL AS val, false AS alone, 
  NULL AS pid, NULL AS sid, n4.* 
FROM
  nexd_dom_entityref AS ref 
LEFT JOIN
  nexd_dom_node AS n4 ON n4.id=ref.nid 
WHERE
  n4.rid=1 
UNION SELECT  
  pi.target AS name, pi.data AS val, false AS alone, 
  NULL AS pid, NULL AS sid, n5.* 
FROM
  nexd_dom_pi AS pi 
LEFT JOIN
  nexd_dom_node AS n5 ON n5.id=pi.nid 
WHERE
  n5.rid=1 
UNION SELECT  
  dtd.name, dtd.subset AS val, false AS alone, 
  dtd.publicId AS pid, dtd.systemId AS sid, n6.* 
FROM
  nexd_dom_doctype AS dtd
LEFT JOIN
  nexd_dom_node AS n6 ON n6.id=dtd.nid 
WHERE
  n6.rid=1 
ORDER BY
  lft;
  
SELECT DISTINCT n2.*, COUNT(*) AS lvl FROM
  nexd_dom_node AS n1,
  nexd_dom_NODE AS n2
WHERE
  n1.rid=1 and n2.rid=1 and
  n2.lft BETWEEN n1.lft AND n1.rgt
GROUP BY
  n2.id, n2.lft, n2.rgt, n2.type, n2.rid
ORDER BY 
  n2.lft;
  

