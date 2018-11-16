import { ILessonFile } from 'app/shared/model//lesson-file.model';
import { ITag } from 'app/shared/model//tag.model';
import { IWord } from 'app/shared/model/word.model';

export interface ILesson {
    id?: number;
    name?: string;
    description?: string;
    lessonFiles?: ILessonFile[];
    rawTags?: string;
    tags?: ITag[];
    words?: IWord[];
}

export class Lesson implements ILesson {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public lessonFiles?: ILessonFile[],
        public tags?: ITag[],
        public rawTags?: string,
        public words?: IWord[]
    ) {}
}
