;#################################################################################
;#
;#  Simple absolute SELECT on a collection over elements
;#
;#################################################################################
SELECT n2.*, e2.name FROM
  nexd_resource AS res,
  nexd_dom_node AS n0,
  nexd_dom_node AS n1,
  nexd_dom_node AS n2,
  nexd_dom_document AS d1,
  nexd_dom_element AS e1,
  nexd_dom_element AS e2
WHERE 
  res.cid=2 AND
  n0.rid=res.id AND n1.rid=res.id AND n2.rid=res.id AND
  d1.nid=n0.id AND n0.lft=1 AND
  e1.nid=n1.id AND e1.name='entity:definition' AND
  n1.lft>n0.lft AND n1.rgt<n0.rgt AND
  ((SELECT COUNT(id) FROM 
     nexd_dom_node
   WHERE rid=res.id AND lft>n0.lft AND lft<n1.lft AND rgt>n1.rgt AND type=1)=0) AND
  e2.nid=n2.id AND e2.name='id' AND 
  n2.lft>n1.lft AND n2.rgt<n1.rgt AND
  ((SELECT COUNT(id) FROM 
     nexd_dom_node
   WHERE rid=res.id AND lft>n1.lft AND lft<n2.lft AND rgt>n2.rgt AND type=1)=0)
GROUP BY 
  n2.id, n2.lft, n2.rgt, n2.type, n2.rid, e2.name;

;#################################################################################
;#
;#  Simple absolute SELECT on a single resource over elements
;#
;#################################################################################
SELECT n2.*, e2.elemname FROM 
  nexd_dom_node AS n0,
  nexd_dom_node AS n1,
  nexd_dom_node AS n2,
  nexd_dom_document AS d1,
  nexd_dom_element AS e1,
  nexd_dom_element AS e2
WHERE 
  n0.rid=2 AND n1.rid=2 AND n2.rid=2 AND
  d1.nid=n0.id AND n0.lft=1 AND
  e1.nid=n1.id AND e1.elemname='entity:definition' AND
  n1.lft>n0.lft AND n1.rgt<n0.rgt AND
  ((SELECT COUNT(id) FROM 
     nexd_dom_node
   WHERE rid=2 AND lft>n0.lft AND lft<n1.lft AND rgt>n1.rgt AND type=1)=0) AND
  e2.nid=n2.id AND e2.elemname='id' AND 
  n2.lft>n1.lft AND n2.rgt<n1.rgt AND
  ((SELECT COUNT(id) FROM 
     nexd_dom_node
   WHERE rid=2 AND lft>n1.lft AND lft<n2.lft AND rgt>n2.rgt AND type=1)=0)
GROUP BY 
  n2.id, n2.lft, n2.rgt, n2.type, n2.rid, e2.elemname;

;/package/maintainers/maintainer[0]
SELECT LIMIT 0 1 xn3.* FROM
  nexd_dom_node AS xn1,
  nexd_dom_node AS xn2,
  nexd_dom_node AS xn3,
  nexd_dom_document AS xd1,
  nexd_dom_element AS xe1,
  nexd_dom_element AS xe2
WHERE
  xn1.rid=7 AND xn2.rid=7 AND xn3.rid=7 AND
  xd1.nid=xn1.id AND xn1.lft=1 AND
  xe1.nid=xn2.id AND xn2.lft>=xn1.lft AND 
  xn2.rgt<=xn1.rgt AND xe1.elemname='package' AND xn2.type=1 AND
  xe2.nid=xn3.id AND xn3.lft>=xn2.lft AND
  xn3.rgt<=xn2.rgt AND xe2.elemname='maintainer' AND xn3.type=1
ORDER BY
  xn3.lft;