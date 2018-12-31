/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { WordsTestDetailComponent } from 'app/entities/words-test/words-test-detail.component';
import { WordsTest } from 'app/shared/model/words-test.model';

describe('Component Tests', () => {
    describe('WordsTest Management Detail Component', () => {
        let comp: WordsTestDetailComponent;
        let fixture: ComponentFixture<WordsTestDetailComponent>;
        const route = ({ data: of({ wordsTest: new WordsTest(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WordsTestDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WordsTestDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WordsTestDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.wordsTest).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
