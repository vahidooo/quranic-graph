# -*- coding: utf-8 -*-
from py2neo import Graph,Node,Relationship,watch

#watch("httpstream")

g = Graph()

result = g.cypher.execute("START n=node:VerseIdx(address = {address}) RETURN n",{"address":"2:3"})
for verse in result:
	print verse[0]
	
result = g.cypher.execute(u"START n=node:TokenIdx( \"simpleArabic:احمد\") RETURN n")
for node in result:
	print node