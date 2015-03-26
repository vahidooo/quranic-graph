# quranic-graph
<div class="align-center">
:&#x202b;۱.از آدرس https://github.com/vahidooo/quranic-graph پروژه رو clone بفرمایید.

۲.یه فولدر داخلش هست به نام neo4j. نئو۴جی ویندوزی خودتون را جایگزینش کنید.به جز فولدر neo4j/conf. یعنی این فولدر همون گیته بمونه.

۳.به داخل فولدر پروژه رفته و دستور زیر را اجرا کنید(قبلش باید ant نصب کرده باشید).پس از مدتی گراف حاضر می‌شود.

ant graph.init

۴.دستور زیر را اجرا کنید:

ant neo4j.plugin.reload

۵.نیو۴جی را بالا بیاورید

./neo4j/bin/neo4j start

۶.در فولدر docs فایلی به نام queries.txt‌ هست که قالب چند کویری نوشته شده است.

۷.برای توسعه پلاگین و بانک کوئری‌ها میتوانیم پکیج server.ext.quran را ملاحظه فرمایید.



اگر پلاگین رو توسعه دادید باید دوکار انجام بدید:

ant neo4j.plugin.reload

./neo4j/bin/neo4j restart
</div>
