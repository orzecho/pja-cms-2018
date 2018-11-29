import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NyanLessonModule } from './lesson/lesson.module';
import { NyanWordModule } from './word/word.module';
import { NyanTagModule } from './tag/tag.module';
import { NyanLessonFileModule } from './lesson-file/lesson-file.module';
import { NyanTestModule } from 'app/entities/test/test.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        NyanLessonModule,
        NyanWordModule,
        NyanTagModule,
        NyanLessonFileModule,
        NyanTestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanEntityModule {}
