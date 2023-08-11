import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnershipDetailComponent } from './ownership-detail.component';

describe('Ownership Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnershipDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OwnershipDetailComponent,
              resolve: { ownership: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OwnershipDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ownership on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OwnershipDetailComponent);

      // THEN
      expect(instance.ownership).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
