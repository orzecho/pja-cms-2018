export interface ILessonFile {
    id?: number;
    name?: string;
    contentContentType?: string;
    content?: any;
    lessonName?: string;
    lessonId?: number;
}

export class LessonFile implements ILessonFile {
    constructor(
        public id?: number,
        public name?: string,
        public contentContentType?: string,
        public content?: any,
        public lessonName?: string,
        public lessonId?: number
    ) {}
}
