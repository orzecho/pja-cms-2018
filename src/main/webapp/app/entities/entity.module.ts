import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NyanLessonModule } from './lesson/lesson.module';
import { NyanWordModule } from './word/word.module';
import { NyanTagModule } from './tag/tag.module';
import { NyanLessonFileModule } from './lesson-file/lesson-file.module';
import { NyanTestModule } from 'app/entities/test/test.module';
import { NyanFillingGapsTestItemModule } from './filling-gaps-test-item/filling-gaps-test-item.module';
import { NyanGapItemModule } from './gap-item/gap-item.module';
import { NyanExamModule } from './exam/exam.module';
import { NyanExamResultModule } from './exam-result/exam-result.module';
import { NyanWrittenAnswerModule } from './written-answer/written-answer.module';
import { NyanTrueFalseAnswerModule } from './true-false-answer/true-false-answer.module';
import { NyanProposedWordModule } from './proposed-word/proposed-word.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        NyanLessonModule,
        NyanWordModule,
        NyanTagModule,
        NyanLessonFileModule,
        NyanTestModule,
        NyanFillingGapsTestItemModule,
        NyanGapItemModule,
        NyanExamModule,
        NyanExamResultModule,
        NyanWrittenAnswerModule,
        NyanTrueFalseAnswerModule,
        NyanProposedWordModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanEntityModule {}
